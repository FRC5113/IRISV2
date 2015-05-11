package gui;

import com.sun.management.OperatingSystemMXBean;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import tools.HistoryArray;

import java.lang.management.ManagementFactory;

/**
 * Created by Jake on 4/26/2015.
 * Manages the page for hardware resource usage, such as network bandwidth, CPU usage, RAM usage, etc.
 */
class ResourceMonitor {

    private OperatingSystemMXBean operatingSystemMXBean;

    private HistoryArray compCPUHist;
    private HistoryArray compRAMHist;


    public ResourceMonitor() {
        operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        compCPUHist = new HistoryArray(50);
        compRAMHist = new HistoryArray(50);
    }

    public void updateHistory() {
        compCPUHist.addHistory(getComputerTotalCPULoad());
        compRAMHist.addHistory((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
    }

    public void updateGraphs(AreaChart compChartCPU, AreaChart compChartRAM) {
        compChartCPU.getData().clear();

        XYChart.Series series = new XYChart.Series();
        series.setName("Computer CPU Usage");

        for (int x = 0; x < 50; x++) {
            series.getData().add(new XYChart.Data("" + x, (int) (compCPUHist.getValueAtTicksAgo(x) * 100)));
        }

        compChartCPU.getData().add(series);


        compChartRAM.getData().clear();

        XYChart.Series seriesRAM = new XYChart.Series();
        seriesRAM.setName("Computer RAM Usage");

        for (int x = 0; x < 50; x++) {
            seriesRAM.getData().add(new XYChart.Data("" + x, (int) (compRAMHist.getValueAtTicksAgo(x))));
        }

        compChartRAM.getData().add(seriesRAM);

    }

    private double getComputerProcessCPULoad() {
        return operatingSystemMXBean.getProcessCpuLoad();
    }

    private double getComputerTotalCPULoad() {
        return operatingSystemMXBean.getSystemCpuLoad();
    }

    private long getComputerFreePhysicalMemory() {
        return operatingSystemMXBean.getFreePhysicalMemorySize();
    }

    private long getComputerCommittedVirtualMemory() {
        return operatingSystemMXBean.getCommittedVirtualMemorySize();
    }


}
