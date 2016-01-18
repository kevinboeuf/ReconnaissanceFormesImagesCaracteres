package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<ImageRelation> imageRelationAttributesList = new ArrayList<ImageRelation>();
        imageRelationAttributesList.add(new ImageRelation(100, 25, FormatAttribute.HORIZONTAL_RECTANGLE));
        imageRelationAttributesList.add(new ImageRelation(110, 26, FormatAttribute.VERTICAL_RECTANGLE));

        ImageRelation.generateARFF(imageRelationAttributesList);
    }
}
