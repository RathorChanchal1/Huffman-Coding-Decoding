public class AsciiElement {
    private String name;
    private int frequency = 0;

    public AsciiElement(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public AsciiElement() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }
}
