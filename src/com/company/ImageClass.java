package com.company;

public enum ImageClass {
    ZERO("Sample001"),
    UN("Sample002"),
    DEUX("Sample003"),
    TROIS("Sample004"),
    QUATRE("Sample005"),
    CINQ("Sample006"),
    SIX("Sample007"),
    SEPT("Sample008"),
    HUIT("Sample009"),
    NEUF("Sample010"),
    A("Sample011"),
    B("Sample012"),
    C("Sample013"),
    D("Sample014"),
    E("Sample015"),
    F("Sample016"),
    G("Sample017"),
    H("Sample018"),
    I("Sample019"),
    J("Sample020"),
    K("Sample021"),
    L("Sample022"),
    M("Sample023"),
    N("Sample024"),
    O("Sample025"),
    P("Sample026"),
    Q("Sample027"),
    R("Sample028"),
    S("Sample029"),
    T("Sample030"),
    U("Sample031"),
    V("Sample032"),
    W("Sample033"),
    X("Sample034"),
    Y("Sample035"),
    Z("Sample036"),
    a("Sample037"),
    b("Sample038"),
    c("Sample039"),
    d("Sample040"),
    e("Sample041"),
    f("Sample042"),
    g("Sample043"),
    h("Sample044"),
    i("Sample045"),
    j("Sample046"),
    k("Sample047"),
    l("Sample048"),
    m("Sample049"),
    n("Sample050"),
    o("Sample051"),
    p("Sample052"),
    q("Sample053"),
    r("Sample054"),
    s("Sample055"),
    t("Sample056"),
    u("Sample057"),
    v("Sample058"),
    w("Sample059"),
    x("Sample060"),
    y("Sample061"),
    z("Sample062");

    String classe;

    ImageClass(String classe) {
        this.classe = classe;
    }

    public static ImageClass getImageClass(String search) {
        for(ImageClass classe : ImageClass.values()) {
            if(search.equals(classe.classe)) {
                return classe;
            }
        }
        return ImageClass.A;
    }
}