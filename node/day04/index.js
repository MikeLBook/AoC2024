const fs = require('node:fs/promises')

async function day04() {
    const data = await fs.readFile('day04.txt', { encoding: 'utf8' })
    const grid = data.split('\n').map(line => line.split('')).filter(line => line.length > 0)

    const findUp = (y, x) => {
        if (y - 3 >= 0) {
            return `${grid[y][x]}${grid[y-1][x]}${grid[y-2][x]}${grid[y-3][x]}`
        } else return ''
    }

    const findDown = (y, x) => {
        if (y + 3 < grid.length) {
            return `${grid[y][x]}${grid[y+1][x]}${grid[y+2][x]}${grid[y+3][x]}`
        } else return ''
    }

    const findLeft = (y, x) => {
        if (x - 3 >= 0) {
            return `${grid[y][x]}${grid[y][x-1]}${grid[y][x-2]}${grid[y][x-3]}`
        } else return ''
    }

    const findRight = (y, x) => {
        if (x + 3 < grid.length) {
            return `${grid[y][x]}${grid[y][x+1]}${grid[y][x+2]}${grid[y][x+3]}`
        } else return ''
    }

    const findUpRight = (y, x) => {
        if (y - 3 >= 0 && x + 3 < grid.length) {
            return `${grid[y][x]}${grid[y-1][x+1]}${grid[y-2][x+2]}${grid[y-3][x+3]}`
        } else return ''
    }

    const findDownRight = (y, x) => {
        if (y + 3 < grid.length && x + 3 < grid.length) {
            return `${grid[y][x]}${grid[y+1][x+1]}${grid[y+2][x+2]}${grid[y+3][x+3]}`
        } else return ''
    }

    const findUpLeft = (y, x) => {
        if (y - 3 >= 0 && x - 3 >= 0) {
            return `${grid[y][x]}${grid[y-1][x-1]}${grid[y-2][x-2]}${grid[y-3][x-3]}`
        } else return ''
    }

    const findDownLeft = (y, x) => {
        if (y + 3 < grid.length && x - 3 >= 0) {
            return `${grid[y][x]}${grid[y+1][x-1]}${grid[y+2][x-2]}${grid[y+3][x-3]}`
        } else return ''
    }

    const part1 = () => {
        let count = 0

        for (let y = 0; y < grid.length; y++) {
            for (let x = 0; x < grid.length; x++) {
                if (grid[y][x] === 'X') {
                    count += [
                        findUp(y, x),
                        findDown(y, x),
                        findLeft(y, x),
                        findRight(y, x),
                        findUpRight(y, x),
                        findDownRight(y, x),
                        findUpLeft(y, x),
                        findDownLeft(y, x)
                    ].filter(word => {
                        return word === 'XMAS'
                    }).length
                }
            }
        }

        return count
    }

    const part2 = () => {
    }

    console.log(`Part 1: ${part1()}`)
    console.log(`Part 2: ${part2()}`)
}

day04()
