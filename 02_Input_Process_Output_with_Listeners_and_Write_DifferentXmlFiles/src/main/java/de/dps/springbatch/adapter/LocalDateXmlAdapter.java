package de.dps.springbatch.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public String marshal(LocalDate localDate) throws Exception {
        String europeanDatePattern = "dd.MM.yyyy";
        DateTimeFormatter europeanDateFormatter
                = DateTimeFormatter.ofPattern(europeanDatePattern);
        String localDateString = europeanDateFormatter.format(localDate);

        return localDateString;
    }

    @Override
    public LocalDate unmarshal(String s) throws Exception {
        return LocalDate.parse(s);
    }
}