package com.koko.gesdi_v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

                        val tempFila = Files.createTempFile("reporte", ".xlsx")
                        workbook.write(tempFila.outputStream())
                        workbook.close()
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
                        val tempFila = Files.createTempFile("reporte", ".pdf")
                        val writer = PdfWriter.getInstance(document, tempFila.outputStream())
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
            }
        }
    }
}