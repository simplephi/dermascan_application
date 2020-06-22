package org.tensorflow.lite.codedirect.dermiscan.ui.main.features.predict

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codedirect.audiometer.utils.SessionManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag_add.btn_predict
import kotlinx.android.synthetic.main.frag_predict.*
import org.tensorflow.lite.codedirect.dermiscan.R
import org.tensorflow.lite.codedirect.dermiscan.ui.main.features.add.Classifier
import org.tensorflow.lite.codedirect.dermiscan.ui.main.features.add.TensorFlowImageClassifier
import org.tensorflow.lite.codedirect.dermiscan.ui.tf.ClassifierActivity
import org.tensorflow.lite.codedirect.dermiscan.utils.Utils
import org.tensorflow.lite.codedirect.dermiscan.utils.hideToolbar

class PredictFrag : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_predict, container, false)
    }

    private val CAMERA_PERMISSION = 100
    private var bitmap: Bitmap? = null
    private val INPUT_SIZE = 148
    private val MODEL_PATH = "model_fp_89_66.tflite"
    private val LABEL_PATH = "model_fp_89_66.txt"
    private lateinit var classifier: Classifier
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private var imageName = ""
    private var imagePath = ""
    private val sessionManager by lazy {
        SessionManager(requireContext())
    }


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
        btn_predict.setOnClickListener(this)
        iv_predict.setOnClickListener(this)
        //btn_predict_realtime.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_predict -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_PERMISSION)
            }
            R.id.btn_predict -> doPredict()
            //R.id.btn_predict_realtime -> doRealtimePredict()
        }
    }

    private fun doRealtimePredict() {
        //startActivity(Intent(requireContext(), ClassifierActivity::class.java))
    }

    private fun doPredict() {
        if (bitmap == null) {
            Utils.toast(requireContext(), getString(R.string.please_choose_img))
        } else {
            bitmap = bitmap?.let {
                Bitmap.createScaledBitmap(it, INPUT_SIZE, INPUT_SIZE, false)
            }
            val results: List<Classifier.Recognition> = classifier.recognizeImage(bitmap)
            btn_predict.text = generateResults(results)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PERMISSION && resultCode == Activity.RESULT_OK) {
            bitmap = data?.extras?.get("data") as Bitmap
            iv_predict.setImageBitmap(bitmap)
            imageName = Utils.getFileName(sessionManager.getIDUser().toString())
            imagePath = bitmap?.let {
                Utils.saveImage(it, context, imageName)
            }.toString()
        }
    }

}