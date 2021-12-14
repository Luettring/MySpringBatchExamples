package de.dps.springbatch.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerXmlAdapter extends XmlAdapter<String, Integer> {

    @Override
    public Integer unmarshal(String v) throws Exception {
        return Integer.valueOf(v);
    }

    @Override
    public String marshal(Integer v) throws Exception {
        return String.valueOf(v.intValue());
    }
}
