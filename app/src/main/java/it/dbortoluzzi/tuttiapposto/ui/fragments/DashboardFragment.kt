package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.databinding.FragmentDashboardBinding
import it.dbortoluzzi.tuttiapposto.model.Score
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.presenters.DashboardPresenter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * DashboardFragment
 */

@AndroidEntryPoint
class DashboardFragment : BaseMvpFragment<DashboardFragment, DashboardPresenter>(), DashboardPresenter.View {

    private lateinit var binding: FragmentDashboardBinding

    @Inject
    override lateinit var presenter: DashboardPresenter

    private lateinit var barChart: BarChart

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        initHourOccupationBarChart()

        return binding.root
    }

    private fun initHourOccupationBarChart() {
//        hide grid lines
        barChart = binding.barChart
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false

        //remove description label
        barChart.description.isEnabled = false

        //add animation
        barChart.animateY(2000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE

        //hide values on columns
        xAxis.setDrawLabels(true)
    }

    override fun renderHourOccupationChart(scores: List<Score>) {
        val barChart = binding.barChart

        //now draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        for (i in scores.indices) {
            val score = scores[i]
            entries.add(BarEntry(i.toFloat(), score.score.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "")
        // simple algorithm to colour the values
        val barColors = gradientBarColours(scores)
        barDataSet.colors = barColors
        barDataSet.setDrawValues(false)

        // update data
        val data = BarData(barDataSet)
        barChart.data = data

        // set formatter for label x axis
        val xAxis: XAxis = barChart.xAxis
        xAxis.valueFormatter = MyAxisFormatter(scores)
        // show all labels
        xAxis.labelCount = scores.size

        findHighligthedIndex(scores.toTypedArray()).apply {
            barChart.highlightValue(this?.toFloat()?:-1F, 0)
        }

        barChart.invalidate()
    }

    private fun findHighligthedIndex(arr: Array<Score>): Int? {
        return (arr.indices)
                .firstOrNull { i: Int -> arr[i].highlighted };
    }

    private fun gradientBarColours(scores: List<Score>): ArrayList<Int> {
        val maxScore = scores.maxOf { it.score }
        val barColors = arrayListOf<Int>()
        for (i in scores.indices) {
            when {
                scores[i].score > maxScore * 0.75 -> {
                    barColors.add(ColorTemplate.rgb("e74c3c"))
                }
                scores[i].score > maxScore * 0.5 -> {
                    barColors.add(ColorTemplate.rgb("#f1c40f"))
                }
                else -> {
                    barColors.add(ColorTemplate.rgb("#2ecc71"))
                }
            }
        }
        return barColors
    }

    inner class MyAxisFormatter(private val scores: List<Score>) : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < scores.size) {
                scores[index].name
            } else {
                ""
            }
        }
    }

    companion object {
        private val TAG = "DashboardFragment"
    }
}
