package com.offcn.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    Map<String,Object> getBusinessReportData();

    List<Integer> getMemberReport(List<String> monthLists);
}
