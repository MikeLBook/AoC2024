const fs = require('node:fs/promises')

async function day04() {
    const data = await fs.readFile('day04.txt', { encoding: 'utf8' })
    const grid = data.split('\n').map(line => line.split('')).filter(line => line.length > 0)

    const part1 = () => {
        let count = 0

        function traceStringAlongGrid(y, x, deltaY, deltaX) {
            if (
                y + (deltaY * 3) >= 0 && 
                y + (deltaY * 3) < grid.length && 
                x + (deltaX * 3) >= 0 && 
                x + (deltaX * 3) < grid.length
            ) {
                return `${grid[y][x]}${grid[y + deltaY][x + deltaX]}${grid[y + (deltaY * 2)][x + (deltaX * 2)]}${grid[y + (deltaY * 3)][x + (deltaX * 3)]}`
            } else return ''
        }

        for (let y = 0; y < grid.length; y++) {
            for (let x = 0; x < grid.length; x++) {
                if (grid[y][x] === 'X') {
                    count += [
                        traceStringAlongGrid(y, x, -1, 0),
                        traceStringAlongGrid(y, x, 1, 0),
                        traceStringAlongGrid(y, x, 0, -1),
                        traceStringAlongGrid(y, x, 0, 1),
                        traceStringAlongGrid(y, x, -1, 1),
                        traceStringAlongGrid(y, x, 1, 1),
                        traceStringAlongGrid(y, x, -1, -1),
                        traceStringAlongGrid(y, x, 1, -1)
                    ].filter(word => {
                        return word === 'XMAS'
                    }).length
                }
            }
        }

        return count
    }

    const part2 = () => {
        let count = 0

        function isPossibleX(y, x) {
            return y - 1 >= 0 
                && x - 1 >= 0
                && x + 1 < grid.length
                && y + 1 < grid.length
        }

        function isValidX(diagonalA, diagonalB) {
            return isValidDiagonal(diagonalA) && isValidDiagonal(diagonalB)    
        }

        function isValidDiagonal(diagonal) {
            return diagonal === 'MAS' || diagonal === 'SAM'
        }

        for (let y = 0; y < grid.length; y++) {
            for (let x = 0; x < grid.length; x++) {
                if (grid[y][x] === 'A' && isPossibleX(y, x)) {
                    const diagonalA = `${grid[y-1][x-1]}A${grid[y+1][x+1]}`
                    const diagonalB = `${grid[y+1][x-1]}A${grid[y-1][x+1]}`

                    if (isValidX(diagonalA, diagonalB)) {
                        count += 1
                    }
                }
            }
        }

        return count
    }

    console.log(`Part 1: ${part1()}`)
    console.log(`Part 2: ${part2()}`)
}

day04()
