package sudoku.newgame.datasave

import sudoku.newgame.sudoku.Board
import java.io.FileReader
import java.io.IOException


class DataHelper {
    fun loadData(): Board {
        var n: Byte? = null
        var input: BooleanArray? = null
        var current: ByteArray? = null
        var correct: ByteArray? = null
        var area: ByteArray? = null
        var pencil: Array<BooleanArray>? = null
        try {
            FileReader("notes3.txt").use { reader ->
                // TODO: Проверки на корректность файла
                // Получаем размер доски
                var c: Int = reader.read()
                n = c.toByte()
                // Инициализируем массивы с данными
                input = BooleanArray(c*c)
                correct = ByteArray(c*c)
                current = ByteArray(c*c)
                area = ByteArray(c*c)
                pencil = Array(c*c) { BooleanArray(c) {false} }
                for(i in 0..n) {
                    c = reader.read()
                    for (z in 0..n) {
                        input[n*i + z] = c % 10 == 1
                        c /= 10
                    }
                }
                for(i in 0..n) {
                    c = reader.read()
                    for (z in 0..n) {
                        correct[n*i + z] = (c % 10).toByte()
                        c /= 10
                    }
                }
                for(i in 0..n) {
                    c = reader.read()
                    for (z in 0..n) {
                        current[n*i + z] = (c % 10).toByte()
                        c /= 10
                    }
                }
                for(i in 0..n) {
                    c = reader.read()
                    for (z in 0..n) {
                        area[n*i + z] = (c % 10).toByte()
                        c /= 10
                    }
                }
                for(i in 0..n*n) {
                    c = reader.read()
                    for (z in 0..n) {
                        pencil[i][z] = c % 10 == 1
                        c /= 10
                    }
                }
            }
        } catch (e: IOException) {

            System.out.println(e.message)
        }
        return Board(n!!, input!!, current!!, correct!!, area!!, pencil!!)
    }
}