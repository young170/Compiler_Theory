import java.util.ArrayList;
import java.util.List;

class ASTNode {
    private String type;
    private String value;
    private List<ASTNode> children;

    public ASTNode() {}

    public ASTNode(String type, String value) {
        this.type = type;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
