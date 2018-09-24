package edu.northeastern.cs5500.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.cs5500.scanner.JPlagServiceScanner;
import edu.northeastern.cs5500.scanner.ScheduledScanner;

/**
 * @author anju
 * @desc Service for producing comparison reports
 */
@RestController
public class ComparisonReportService {
    Logger logger = Logger.getLogger(ComparisonReportService.class.getName());
    private static final String reportDirPath = JPlagServiceScanner.getReportPath();

    public ComparisonReportService() {

    }

    /**
     * Method to get the reports
     * @param name is a string
     * @param htmlname is a string
     * @return a byte array
     */
    @RequestMapping(value="/api/report/{name}/{html}.html", method={RequestMethod.GET})
    public @ResponseBody byte[] getReport(@PathVariable(name="name") String name,
                                          @PathVariable(name="html") String htmlname)  {
        String reportPath = reportDirPath + name + "/" + htmlname + ".html";
        File reportFile = new File(reportPath);
        if(reportFile.exists()) {
            try {
                return IOUtils.toByteArray(new FileInputStream(reportFile));
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        return new byte[0];
    }

    /**
     * Method to get the image
     * @param name is a string
     * @param imagename is a string
     * @return a byte array
     */
    @RequestMapping(value="/api/report/{name}/{image}.gif", method={RequestMethod.GET})
    public @ResponseBody byte[] getImage(@PathVariable(name="name") String name,
                                         @PathVariable(name="image") String imagename)  {
        String reportPath = reportDirPath + name + "/" + imagename + ".gif";
        File reportFile = new File(reportPath);
        if(reportFile.exists()) {
            try {
                return IOUtils.toByteArray(new FileInputStream(reportFile));
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        return new byte[0];
    }

    /**
     * Method to enable scanning
     * @param enable is a boolean
     * @return a String
     */
    @RequestMapping(value="/api/incrementalscanner/{enable}", method={RequestMethod.GET})
    public @ResponseBody String enableScanning(@PathVariable(name="enable") boolean enable)  {
        ScheduledScanner.setScanEnabled(enable);
        if (ScheduledScanner.isScanEnabled()) {
            return "Scanner enabled";
        } else {
            return "Scanner disabled";
        }
    }

}
