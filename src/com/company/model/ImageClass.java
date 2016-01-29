package com.company.model;

public enum ImageClass {
    ZERO("Sample001", 1),
    UN("Sample002", 2),
    DEUX("Sample003",3),
    TROIS("Sample004", 4),
    QUATRE("Sample005", 5),
    CINQ("Sample006", 6),
    SIX("Sample007", 7),
    SEPT("Sample008", 8),
    HUIT("Sample009", 9),
    NEUF("Sample010", 10),
    A("Sample011", 11),
    B("Sample012", 12),
    C("Sample013", 13),
    D("Sample014", 14),
    E("Sample015", 15),
    F("Sample016", 16),
    G("Sample017", 17),
    H("Sample018", 18),
    I("Sample019", 19),
    J("Sample020", 20),
    K("Sample021", 21),
    L("Sample022", 22),
    M("Sample023", 23),
    N("Sample024", 24),
    O("Sample025", 25),
    P("Sample026", 26),
    Q("Sample027", 27),
    R("Sample028", 28),
    S("Sample029", 29),
    T("Sample030", 30),
    U("Sample031", 31),
    V("Sample032", 32),
    W("Sample033", 33),
    X("Sample034", 34),
    Y("Sample035", 35),
    Z("Sample036", 36),
    a("Sample037", 37),
    b("Sample038", 28),
    c("Sample039", 39),
    d("Sample040", 40),
    e("Sample041", 41),
    f("Sample042", 42),
    g("Sample043", 43),
    h("Sample044", 44),
    i("Sample045", 45),
    j("Sample046", 46),
    k("Sample047", 47),
    l("Sample048", 48),
    m("Sample049", 49),
    n("Sample050", 50),
    o("Sample051", 51),
    p("Sample052", 52),
    q("Sample053", 53),
    r("Sample054", 54),
    s("Sample055", 55),
    t("Sample056", 56),
    u("Sample057", 57),
    v("Sample058", 58),
    w("Sample059", 59),
    x("Sample060", 60),
    y("Sample061", 61),
    z("Sample062", 62);

    public String path;
    public Integer number;

    ImageClass(String path, Integer number) {
        this.path = path;
        this.number = number;
    }

    public static ImageClass getImageClass(String search) {
        for(ImageClass classe : ImageClass.values()) {
            if(search.equals(classe.path)) {
                return classe;
            }
        }
        return ImageClass.ZERO;
    }

    public static ImageClass getImageClassFromNumber(int number) {
        for(ImageClass classe : ImageClass.values()) {
            if(number == classe.number) {
                return classe;
            }
        }
        return ImageClass.ZERO;
    }
}