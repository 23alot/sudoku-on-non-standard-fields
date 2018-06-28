package sudoku.newgame.datasave

import sudoku.newgame.sudoku.Board
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException


class DataHelper {
    fun loadData(): Board {
        var n: Int? = null
        var input: BooleanArray? = null
        var current: ByteArray? = null
        var correct: ByteArray? = null
        var area: ByteArray? = null
        var pencil: Array<BooleanArray>? = null
        try {
            var reader = FileReader("notes3.txt")
                // TODO: Проверки на корректность файла
                // Получаем размер доски
                var c: Int = reader.read()
                var n = c
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
        } catch (e: IOException) {

            System.out.println(e.message)
        }
        return Board(n!!.toByte(), input!!, current!!, correct!!, area!!, pencil!!)
    }
    fun saveData(board: Board) {
        try {
            var writer = FileWriter("notes3.txt")
            // TODO: Проверки на корректность файла
            // Получаем размер доски
            writer.write(board.N.toInt())
            for(i in 0..board.N) {
                var c = 0
                for (z in 0..board.N) {
                    c *= 10
                    if(board.cells[i][z].isInput) c += 1
                }
                writer.write(c)
            }
            for(i in 0..board.N) {
                var c = 0
                for (z in 0..board.N) {
                    c *= 10
                    c += board.cells[i][z].correctValue
                }
                writer.write(c)
            }
            for(i in 0..board.N) {
                var c = 0
                for (z in 0..board.N) {
                    c *= 10
                    c += board.cells[i][z].value
                    // TODO: сделать дефолт значение 0, а не -1
                }
                writer.write(c)
            }
            for(i in 0..board.N) {
                var c = 0
                for (z in 0..board.N) {
                    c *= 10
                    c += board.areas[i*board.N+z]
                }
                writer.write(c)
            }
            for(i in 0..board.N*board.N) {
                var c = 0
                for (z in 0..board.N) {
                    c += if(board.cells[i/board.N][i%board.N].possibleValues[z]) 1 else 0
                    c *= 10
                }
                writer.write(c)
            }

        } catch (e: IOException) {

            System.out.println(e.message)
        }
    }
}
