package com.exa863.anselmo_adna.view.fx;

/**
 * Lista todos os ícones SVG disponíveis em resources/images/icons.
 *
 * @author Anselmo e Adna
 */
public enum Icone {

    ASPERAND_SIGN("Asperand-Sign"),
    BACKWARD("Backward"),
    BAG("Bag"),
    BOOKMARK("Bookmark"),
    BULLETED_LIST("Bulleted-List"),
    CALENDER("Calender"),
    CART("Cart"),
    CD("CD"),
    CHEVRON_ARROW_DOWN("Chevron-Arrow-Down"),
    CHEVRON_ARROW_LEFT("Chevron-Arrow-Left"),
    CHEVRON_ARROW_RIGHT("Chevron-Arrow-Right"),
    CHEVRON_ARROW_UP("Chevron-Arrow-Up"),
    CLOCK("Clock"),
    CURSOR_0("Cursor-0"),
    CURSOR_1("Cursor-1"),
    CURSOR_2("Cursor-2"),
    CURSOR_3("Cursor-3"),
    CURSOR_4("Cursor-4"),
    CURSOR_5("Cursor-5"),
    CURSOR_6("Cursor-6"),
    DISK("Disk"),
    DIVIDE_SIGN("Divide-Sign"),
    DOUBLE_CHEVRON_ARROW_LEFT("Double-Chevron-Arrow-Left"),
    DOUBLE_CHEVRON_ARROW_RIGHT("Double-Chevron-Arrow-Right"),
    DOWN_ARROW("Down-Arrow"),
    DOWNLOAD("Download"),
    EXCLAMATION_MARK("Exclamation-Mark"),
    EXIT("Exit"),
    FILE("File"),
    FILE_COMPRESSED("File-Compressed"),
    FILE_IMAGE("File-Image"),
    FILE_MUSIC("File-Music"),
    FILE_SCRIPT("File-Script"),
    FILE_TEXT("File-Text"),
    FILTER("Filter"),
    FLOPPY_DRIVE("Floppy-Drive"),
    FOLDER("Folder"),
    FOLDER_COMPRESSED("Folder-Compressed"),
    FOLDER_HOME("Folder-Home"),
    FOLDER_IMAGE("Folder-Image"),
    FOLDER_MUSIC("Folder-Music"),
    FOLDER_SCRIPT("Folder-Script"),
    FORWARD("Forward"),
    GAME_CONTROLLER("Game-Controller"),
    GEAR("Gear"),
    GRID("Grid"),
    HEART("Heart"),
    HOME("Home"),
    INFO("Info"),
    LEFT_ARROW("Left-Arrow"),
    LIST("List"),
    LOCATION("Location"),
    LOCK_CLOSED("Lock-Closed"),
    LOCK_OPEN("Lock-Open"),
    MAGNIFYING_GLASS("Magnifying-Glass"),
    MAGNIFYING_GLASS_ADD("Magnifying-Glass-Add"),
    MAGNIFYING_GLASS_REDUCE("Magnifying-Glass-Reduce"),
    MAIL("Mail"),
    MESSAGE("Message"),
    MESSAGE_EXCLAMATION("Message-Exclamation"),
    MESSAGE_PLUS("Message-Plus"),
    MESSAGE_QUESTION("Message-Question"),
    MESSAGE_THREE_DOTS("Message-Three-Dots"),
    MICROPHONE("Microphone"),
    MICROPHONE_CROSSED("Microphone-Crossed"),
    MOUSE("Mouse"),
    MOUSE_BOTH("Mouse-Both"),
    MOUSE_LEFT("Mouse-Left"),
    MOUSE_MIDDLE("Mouse-Middle"),
    MOUSE_RIGHT("Mouse-Right"),
    MUSIC_0("Music-0"),
    MUSIC_1("Music-1"),
    NEXT("Next"),
    PAUSE("Pause"),
    PEOPLE("People"),
    PEOPLE_CROSSED("People-Crossed"),
    PERCENTAGE("Percentage"),
    PERSON("Person"),
    PERSON_CROSSED("Person-Crossed"),
    PLAY("Play"),
    PLUS("Plus"),
    POWER("Power"),
    PREVIOUS("Previous"),
    QUESTION_MARK("Question-Mark"),
    REDO("Redo"),
    RIGHT_ARROW("Right-Arrow"),
    SHARE("Share"),
    SIGNAL_0("Signal-0"),
    SIGNAL_1("Signal-1"),
    SIGNAL_2("Signal-2"),
    SLASH("Slash"),
    SPEAKER_0("Speaker-0"),
    SPEAKER_1("Speaker-1"),
    SPEAKER_CROSSED("Speaker-Crossed"),
    STACK("Stack"),
    STAR("Star"),
    THREE_DOTS_HORIZONTAL("Three-Dots-Horizontal"),
    THREE_DOTS_VERTICAL("Three-Dots-Vertical"),
    UP_ARROW("Up-Arrow"),
    UPLOAD("Upload");

    private static final String PASTA_ICONS = "/images/icons/";
    private static final String EXTENSAO = ".svg";

    private final String caminhoRelativo;

    Icone(String caminhoRelativo) {
        this.caminhoRelativo = caminhoRelativo;
    }

    public String getCaminho() {
        return PASTA_ICONS + caminhoRelativo + EXTENSAO;
    }
}
