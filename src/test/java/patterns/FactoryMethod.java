package patterns;

interface Button {
    void render();
    void onClick();
}

class HTMLButton implements Button {
    public void render() {
        System.out.println("THIS is button for HTML");
    }

    public void onClick() {
        System.out.println("HTML button has been clicked");
    }
}


class WindowsButton implements Button {
    public void render() {
        System.out.println("THIS is button for windows");
    }

    public void onClick() {
        System.out.println("Windows button has been clicked");
    }
}

abstract class Creator {
    abstract Button createButton();
}

class HTMLCreator extends Creator {
    Button createButton() {
        return new HTMLButton();
    }
}

class WindowsCreator extends Creator {
    Button createButton() {
        return new WindowsButton();
    }
}

public class FactoryMethod {
    public static void main(String[] args) {
        Creator html = new HTMLCreator();
        Button htmlButton = html.createButton();
        htmlButton.onClick();
    }
}
