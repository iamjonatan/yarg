package com.haulmont.yarg.reporting;


import java.io.OutputStream;

public interface ReportingAPI {
    ReportOutputDocument runReport(RunParams runParams, OutputStream outputStream);

    ReportOutputDocument runReport(RunParams runParams);
}