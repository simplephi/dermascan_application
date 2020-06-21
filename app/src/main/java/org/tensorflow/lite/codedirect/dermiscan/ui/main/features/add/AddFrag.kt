package org.tensorflow.lite.codedirect.dermiscan.ui.main.features.add

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.codedirect.audiometer.utils.SessionManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.act_login.*
import kotlinx.android.synthetic.main.frag_add.*
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.codedirect.dermiscan.R
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.ResponseJSON
import org.tensorflow.lite.codedirect.dermiscan.utils.Utils
import org.tensorflow.lite.codedirect.dermiscan.utils.common.Status
import org.tensorflow.lite.codedirect.dermiscan.utils.hideToolbar

class AddFrag : Fragment(), View.OnClickListener {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_add, container, false)
    }

    private val CAMERA_PERMISSION = 100
    private val GALLERY = 101
    private var imageName = ""
    private var imagePath = ""
    private val sessionManager by lazy {
        SessionManager(requireContext())
    }

    private lateinit var bitmap: Bitmap
    private val INPUT_SIZE = 150
//    private val MODEL_PATH = "model_fp_72.tflite"
//    private val LABEL_PATH = "model_fp_72.txt"
    private val MODEL_PATH = "model_fp_89_66.tflite"
    private val LABEL_PATH = "model_fp_89_66.txt"
    private lateinit var classifier: Classifier
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val model by viewModel<AddVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolbar()
        initTensorFlow()?.subscribe(object : CompletableObserver {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                Log.e("initTensorFlow", e?.message)
            }
        })
        setupClickListener()
    }

    override fun onDestroy() {
        closeClassifier().subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onComplete() {
                Log.i("closeClassifier", "completed")
            }

            override fun onError(e: Throwable) {
                Log.e("closeClassifier", e.message)
            }
        })
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun closeClassifier(): Completable {
        return Completable.fromAction { classifier.close() }.subscribeOn(Schedulers.newThread())
    }

    private fun generateResults(data: List<Classifier.Recognition>): String? {
        var result = ""
        for (i in data.indices) {
            result = """
                $result
                ${data[i]}
                """.trimIndent()
        }
        return result
    }

    private fun initTensorFlow(): Completable? {
        return Completable.fromAction {
            classifier = TensorFlowImageClassifier.create(
                    requireActivity().assets,
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE)
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    private fun setupClickListener() {
        btn_add_photo.setOnClickListener(this)
        btn_predict.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_photo -> {
                //addPhoto()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_PERMISSION)
            }
            R.id.btn_predict -> doPredict()
            R.id.btn_submit -> doSubmit()
        }
    }

    private fun doSubmit() {
        val map: MutableMap<String, RequestBody> = HashMap()
        map["title"] = Utils.getRequestBody(ed_add_img_name.text.toString())
        map["label"] = Utils.getRequestBody(ed_add_desc.text.toString())
        map["user_id"] = Utils.getRequestBody(sessionManager.getIDUser().toString())
        map["predicted"] = Utils.getRequestBody("")

        model.uploadImagess(
                Utils.getMultipartBodyUpload(imagePath),
                Utils.getRequestBody(ed_add_img_name.text.toString()),
                Utils.getRequestBody(btn_predict.text.toString()),
                Utils.getRequestBody(sessionManager.getIDUser().toString())
        ).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loading_add.visibility = View.GONE
                        resource.data.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        loading_add.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        loading_add.visibility = View.VISIBLE
                    }
                }
            }
        })

        /*model.uploadImages(
                Utils.getMultipartBodyUpload(imagePath),
                map
        ).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loading_add.visibility = View.GONE
                        resource.data.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        loading_add.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        loading_add.visibility = View.VISIBLE
                    }
                }
            }
        })*/

        /*model.upload(
                Utils.getMultipartBodyUpload(imagePath)
        ).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loading_add.visibility = View.GONE
                        resource.data.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        loading_add.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        loading_add.visibility = View.VISIBLE
                    }
                }
            }
        })*/
    }

    private fun retrieveList(users: ResponseJSON?) {
        Utils.toast(requireContext(), getString(R.string.success_))
        resetData()
    }

    private fun resetData() {
        ed_add_img_name.text = SpannableStringBuilder("")
        ed_add_age.text = SpannableStringBuilder("")
        ed_add_desc.text = SpannableStringBuilder("")
        iv_add_picture.setImageResource(R.drawable.mini_logo)
        btn_predict.text = getString(R.string.predict)
    }

    private fun doPredict() {
        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
        val results: List<Classifier.Recognition> = classifier.recognizeImage(bitmap)
        btn_predict.text = generateResults(results)
    }

    private fun addPhoto() {
        Utils.formDialogList(requireContext(), arrayListOf(getString(R.string.camera_), getString(R.string.gallery_))) { materialDialog: MaterialDialog, i: Int ->
            when (i) {
                0 -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_PERMISSION)
                }
                1 -> {
                    val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PERMISSION && resultCode == Activity.RESULT_OK) {
            bitmap = data?.extras?.get("data") as Bitmap
            iv_add_picture.setImageBitmap(bitmap)
            imageName = Utils.getFileName(sessionManager.getIDUser().toString())
            imagePath = Utils.saveImage(bitmap, context, imageName)
        }
    }
}