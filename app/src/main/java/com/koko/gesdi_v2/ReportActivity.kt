package com.koko.gesdi_v2

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.koko.gesdi_v2.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.nio.file.Files
import kotlin.io.path.outputStream
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class ReportActivity : AppCompatActivity() {
    private lateinit var inputMes: EditText
    private lateinit var inputAnio: EditText
    private lateinit var btnDashboard: ConstraintLayout
    private lateinit var btnExcel: Button
    private lateinit var btnPdf: Button
    private var userId: Int = 0
    private var monthNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        cargarDatos()
        asignarReferencias()
    }

    private fun cargarDatos() {
        userId = intent.getIntExtra("user_id", 1)
    }

    private fun asignarReferencias() {
        inputMes = findViewById(R.id.inputMesRep)
        inputAnio = findViewById(R.id.inputAnioRep)
        btnDashboard = findViewById(R.id.btnReportToDashboard)
        btnExcel = findViewById(R.id.btnExcel)
        btnPdf = findViewById(R.id.btnPdf)

        inputMes.setOnClickListener {
            val dialog = MonthPickerDialog()
            dialog.setListener { month, monthName ->
                inputMes.setText(monthName)
                monthNumber = month
            }
            dialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }

        inputAnio.setOnClickListener {
            val dialog = YearPickerDialog()
            dialog.setListener { year ->
                inputAnio.setText(year.toString())
            }
            dialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }

        btnDashboard.setOnClickListener {
            finish()
        }

        btnExcel.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val rpta = RetrofitClient.webService.getReporte(userId, monthNumber, inputAnio.text.toString().toInt())
                runOnUiThread {
                    if (rpta.isSuccessful) {
                        val reportes = rpta.body()!!.listaTransacciones

                        val workbook = XSSFWorkbook()
                        val sheet = workbook.createSheet("Reporte")
                        val firstRow = sheet.createRow(0)
                        firstRow.createCell(0).setCellValue("Fecha de Transacción")
                        firstRow.createCell(1).setCellValue("Monto de Transacción")
                        firstRow.createCell(2).setCellValue("Descripción de Transacción")
                        firstRow.createCell(3).setCellValue("Categoría de Transacción")
                        firstRow.createCell(4).setCellValue("Tipo de Transacción")

                        var rowNum = 1

                        for (reporte in reportes) {
                            val row = sheet.createRow(rowNum++)
                            row.createCell(0).setCellValue(reporte.transaction_date)
                            row.createCell(1).setCellValue(reporte.transaction_amount)
                            row.createCell(2).setCellValue(reporte.transaction_description)
                            row.createCell(3).setCellValue(reporte.category_name)
                            row.createCell(4).setCellValue(reporte.type_name)
                        }

                        val resolver = contentResolver
                        val contentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, "Reporte.xlsx")
                            put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/")
                        }

                        val uri: Uri? = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

                        uri?.let {
                            resolver.openOutputStream(it).use { outputStream ->
                                workbook.write(outputStream)
                            }
                        }

                        workbook.close()

                        Toast.makeText(this@ReportActivity, "Reporte.xlsx generado", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        btnPdf.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val rpta = RetrofitClient.webService.getReporte(userId, monthNumber, inputAnio.text.toString().toInt())
                runOnUiThread {
                    if (rpta.isSuccessful) {
                        val reportes = rpta.body()!!.listaTransacciones

                        val document = Document()
                        val resolver = contentResolver
                        val contentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, "Reporte.pdf")
                            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                        }

                        val uri: Uri? = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

                        uri?.let {
                            resolver.openOutputStream(it).use { outputStream ->
                                val writer = PdfWriter.getInstance(document, outputStream)
                                document.open()

                                val table = PdfPTable(5)
                                table.addCell("Fecha de Transacción")
                                table.addCell("Monto de Transacción")
                                table.addCell("Descripción de Transacción")
                                table.addCell("Categoría de Transacción")
                                table.addCell("Tipo de Transacción")

                                for (reporte in reportes) {
                                    table.addCell(reporte.transaction_date)
                                    table.addCell(reporte.transaction_amount.toString())
                                    table.addCell(reporte.transaction_description)
                                    table.addCell(reporte.category_name)
                                    table.addCell(reporte.type_name)
                                }

                                document.add(table)
                                document.close()
                            }
                        }

                        Toast.makeText(this@ReportActivity, "Reporte.pdf generado", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}