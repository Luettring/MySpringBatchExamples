package de.dps.springbatch.adapter;

import de.dps.springbatch.model.Gender;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GenderXmlAdapter extends XmlAdapter<String, Gender> {

    @Override
    public String marshal(Gender gender) throws Exception {
        if (gender.name().equals("F")) {
            return "W";
        }
        else if (gender.name().equals("M")) {
            return "M";
        }

        throw new IllegalArgumentException("Ungültiges Geschlecht!");
//        return "U";
    }

    @Override
    public Gender unmarshal(String s) throws Exception {
        if (s.toUpperCase().equals("W")) {
            return Gender.F;
        }
        else if (s.toUpperCase().equals("M")) {
            return Gender.M;
        }
        throw new IllegalArgumentException("Ungültiges Geschlecht!");
//        return Gender.U;
    }
}