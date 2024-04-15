package lando.systems.ld55.entities;

public enum Pattern {
    PAWN_ATK(new char[][] {
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', '1', ' ', ' ', ' '},
        {' ', ' ', '1', 'x', '1', ' ', ' '},
        {' ', ' ', ' ', '1', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '}
    }),
    PAWN_MOV(new char[][] {
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', '1', ' ', ' ', ' '},
        {' ', ' ', '1', 'x', '1', ' ', ' '},
        {' ', ' ', ' ', '1', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '}
    }),
    // -------------------------------------
    BISHOP_ATK(new char[][] {
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', '2', ' ', '2', ' ', ' '},
        {' ', ' ', ' ', 'x', ' ', ' ', ' '},
        {' ', ' ', '2', ' ', '2', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '}
    }),
    BISHOP_MOV(new char[][] {
        {'1', ' ', ' ', ' ', ' ', ' ', '1'},
        {' ', '1', ' ', ' ', ' ', '1', ' '},
        {' ', ' ', '1', ' ', '1', ' ', ' '},
        {' ', ' ', ' ', 'x', ' ', ' ', ' '},
        {' ', ' ', '1', ' ', '1', ' ', ' '},
        {' ', '1', ' ', ' ', ' ', '1', ' '},
        {'1', ' ', ' ', ' ', ' ', ' ', '1'}
    }),
    // -------------------------------------
    KNIGHT_ATK(new char[][] {
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', '1', '1', '1', ' ', ' '},
        {' ', '1', ' ', ' ', ' ', '1', ' '},
        {' ', '1', ' ', 'x', ' ', '1', ' '},
        {' ', '1', ' ', ' ', ' ', '1', ' '},
        {' ', ' ', '1', '1', '1', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '}
    }),
    ARCHER_MOV(new char[][] {
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', '1', '1', '1', ' ', ' '},
        {' ', ' ', '1', 'x', '1', ' ', ' '},
        {' ', ' ', '1', '1', '1', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' '}
    }),
    // -------------------------------------
    QUEEN_ATK(new char[][] {
        {' ', ' ', ' ', '1', ' ', ' ', ' '},
        {' ', '1', ' ', '2', ' ', '1', ' '},
        {' ', ' ', '2', '3', '2', ' ', ' '},
        {'1', '2', '3', 'x', '3', '2', '1'},
        {' ', ' ', '2', '3', '2', ' ', ' '},
        {' ', '1', ' ', '2', ' ', '1', ' '},
        {' ', ' ', ' ', '1', ' ', ' ', ' '}
    }),
    // -------------------------------------
    ROOK_ATK(new char[][] {
        {' ', ' ', ' ', '2', ' ', ' ', ' '},
        {' ', ' ', ' ', '2', ' ', ' ', ' '},
        {' ', ' ', ' ', '1', ' ', ' ', ' '},
        {'2', '2', '1', 'x', '1', '2', '2'},
        {' ', ' ', ' ', '1', ' ', ' ', ' '},
        {' ', ' ', ' ', '2', ' ', ' ', ' '},
        {' ', ' ', ' ', '2', ' ', ' ', ' '}
    }),
    ;

    public static final int size = 7;

    public final char[][] vals;

    Pattern(char[][] vals) {
        this.vals = vals;
    }

    public Pattern next__TEST() {
        var next = (ordinal() + 1) % Pattern.values().length;
        return Pattern.values()[next];
    }
}
