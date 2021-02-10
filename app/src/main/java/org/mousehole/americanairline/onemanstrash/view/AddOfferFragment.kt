package org.mousehole.americanairline.onemanstrash.view

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.data.repository.FileSaver
import org.mousehole.americanairline.onemanstrash.model.Offer
import org.mousehole.americanairline.onemanstrash.utils.DebugLogger
import org.mousehole.americanairline.onemanstrash.utils.DebugLogger.debug
import org.mousehole.americanairline.onemanstrash.utils.QuickToast.showMessage
import org.mousehole.americanairline.onemanstrash.validation.Fail
import org.mousehole.americanairline.onemanstrash.validation.Pass
import org.mousehole.americanairline.onemanstrash.validation.PassFail
import org.mousehole.americanairline.onemanstrash.validation.Validation
import org.mousehole.americanairline.onemanstrash.viewmodel.ListingViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddOfferFragment : Fragment(), FileSaver.FileSaverCallback {

    private val listingViewModel = ListingViewModel
    
    private lateinit var offerImageView: ImageView
    private lateinit var offerNameEditText: EditText
    private lateinit var offerExpirationTextEdit: TextView
    private lateinit var offerDescriptionEditText: EditText
    private lateinit var offerButton: Button

    private var fileDir : String? = null

    private var offerBitmap : Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.offer_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offerImageView = view.findViewById(R.id.offer_imageview)
        offerNameEditText = view.findViewById(R.id.offer_name_edittext)
        offerExpirationTextEdit = view.findViewById(R.id.expiration_textview)
        offerDescriptionEditText = view.findViewById(R.id.offer_description_edittext)
        offerButton = view.findViewById(R.id.offer_button)

        offerButton.setOnClickListener {
            offerButton.isEnabled = false
            when (val t = validations()) {
                is Fail -> {
                    showMessage(requireContext(), t.failMessages)
                    offerButton.isEnabled = true
                }
                is Pass -> {
                    val offer = Offer(name = offerNameEditText.cleanEditText(),
                    description = offerDescriptionEditText.cleanEditText(),
                    expiration = offerExpirationTextEdit.text.toString(),
                    seller = listingViewModel.getUid(),
                    email = listingViewModel.getEmail())
                    FileSaver(this).saveFile(requireContext(), offer, offerBitmap)
                }

            }
        }

        val today = GregorianCalendar()

        displayDate(today.time)
        debug("What is today...? $today")

        offerExpirationTextEdit.setOnClickListener {
            val onDateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    val expires = GregorianCalendar(year, month, dayOfMonth).time
                    displayDate(expires)
                }
            }
            DatePickerDialog(requireContext(),
                    onDateSetListener,
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH))
                    .show()
        }

        offerImageView.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val bundle = Bundle()
            context?.let {
                if (cameraIntent.resolveActivity(it.packageManager) != null) {
                    // establiches camera exists

                    try {
                        val tmpDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        val tmpFile = File.createTempFile("delete_me","png", tmpDir)
                        tmpFile.let { file ->
                            val imageUri = FileProvider.getUriForFile(
                                    it,
                                    "org.mousehole.americanairline.onemanstrash.fileprovider",
                                    file)
                            fileDir = tmpFile.absolutePath
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                            startActivityForResult(cameraIntent, 111)
                        }

                    } catch (e: Exception) {
                        DebugLogger.error(e)
                        showMessage(requireContext(), "Something went wrong, and image could not be grabbed.")
                    }
                }
            }
        }
    }


    private val dateFormat = SimpleDateFormat("MM/dd/yyyy")

    private fun displayDate(date:Date) {
        offerExpirationTextEdit.text = dateFormat.format(date)
    }

    private fun validations() : PassFail {
        val dateString = offerExpirationTextEdit.text.toString()
        val currentDate = dateFormat
                .parse(dateString)
        return Validation({offerNameEditText.cleanEditText().isNotBlank()},
                "Please provide a descriptive name for your item.")
                .chain({offerDescriptionEditText.cleanEditText().isNotBlank()},
                "Description cannot be empty.")
                .chain( {GregorianCalendar().time.time < currentDate.time},
        "Your expiration date should be in the future.")
                .chain({fileDir?.trim()?.isNotBlank()?:false},
                "Please include an image of your item")
            .exec()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        offerBitmap = BitmapFactory.decodeFile(fileDir)
        offerBitmap?.let {
            offerImageView.setImageBitmap(it)
        }
    }

    override fun reset() {
        offerImageView.setImageDrawable(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_image, null))
        offerNameEditText.text.clear()
        offerDescriptionEditText.text.clear()
        displayDate(GregorianCalendar().time)
        offerBitmap = null
        fileDir = null
        offerButton.isEnabled = true
    }

    private fun EditText.cleanEditText() = this.text.toString().trim()
}