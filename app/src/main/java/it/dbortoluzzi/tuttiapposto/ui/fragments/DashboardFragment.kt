package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentDashboardBinding
import it.dbortoluzzi.tuttiapposto.model.Score
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.presenters.DashboardPresenter
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        initHourOccupationBarChart()
        initRoomOccupationBarChart()

        return binding.root
    }

    private fun initHourOccupationBarChart() {
        val barChart = binding.hourOccupationBarChart
        initCustomBarChart(barChart)
    }

    private fun initRoomOccupationBarChart() {
        val barChart = binding.roomOccupationBarChart
        initCustomBarChart(barChart)
    }

    private fun initCustomBarChart(barChart: BarChart) {
        barChart.setPinchZoom(false)
        barChart.setScaleEnabled(false)
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

        xAxis.granularity = 1.0f;
        xAxis.isGranularityEnabled = true; // Required to enable granularity

        barChart.axisLeft.granularity = 1.0f;
        barChart.axisLeft.isGranularityEnabled = true; // Required to enable granularity
    }

    override fun renderHourOccupationChart(scores: List<Score>) {
        val barChart = binding.hourOccupationBarChart

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

    override fun renderRoomOccupationChart(scores: List<Score>) {
        val barChart = binding.roomOccupationBarChart

        //now draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        for (i in scores.indices) {
            val score = scores[i]
            entries.add(BarEntry(i.toFloat(), score.score.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "")
        // simple algorithm to colour the values
        val barColors = percentBarColours(scores)
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
        var yAxis = barChart.axisLeft
        yAxis.valueFormatter = MyPercentAxisFormatter()
        // TODO: set max value on Y axis

        barChart.invalidate()
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE

        if (activity is MainPresenter.View) {
            (activity as MainPresenter.View).closeNavigationDrawer()
        }
    }

    override fun showNetworkError() {
        Toast.makeText(context() , getString(R.string.network_not_connected), Toast.LENGTH_LONG).show()
    }

    private fun findHighligthedIndex(arr: Array<Score>): Int? {
        return (arr.indices)
                .firstOrNull { i: Int -> arr[i].highlighted };
    }

    private fun gradientBarColours(scores: List<Score>): ArrayList<Int> {
        val maxScore = if (scores.isNotEmpty()) scores.maxOf { it.score } else 0
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

    private fun percentBarColours(scores: List<Score>): ArrayList<Int> {
        val barColors = arrayListOf<Int>()
        for (i in scores.indices) {
            when {
                scores[i].score > 50 -> {
                    barColors.add(ColorTemplate.rgb("e74c3c"))
                }
                scores[i].score > 40 -> {
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

    inner class MyPercentAxisFormatter() : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return "$value %"
        }
    }

    companion object {
        private val TAG = "DashboardFragment"
    }
}
