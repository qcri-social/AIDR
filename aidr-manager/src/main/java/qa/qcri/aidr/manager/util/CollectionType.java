/**
 * Created with IntelliJ IDEA.
 * User: mjr
 * Date: 23.07.14
 * Time: 22:19
 */
package qa.qcri.aidr.manager.util;

public enum CollectionType {
    Twitter("tweet", "tweets"),
    SMS("sms", "sms");

    private String singular;
    private String plural;

    private CollectionType(String singular, String plural) {
        this.singular = singular;
        this.plural = plural;
    }

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }

    public static String JSON(){
        StringBuilder builder = new StringBuilder("{");
        for (CollectionType type : values()) {
           builder.append(type.name()).append(":{").append("singular:\"").append(type.getSingular()).append("\",plural:\"").append(type.getPlural()).append("\"},");
        }
        return builder.append("}").toString();
    }
}
