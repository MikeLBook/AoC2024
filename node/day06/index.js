/**
 * Solution assumes all inputs start with the guard facing up.
 */

const fs = require('node:fs/promises')

day06()

const directions = {
    'up': {x: 0, y: -1},
    'down': {x: 0, y: 1},
    'left': {x: -1, y: 0},
    'right': {x: 1, y: 0}
}

async function day06() {
    const data = await fs.readFile('day06.txt', { encoding: 'utf8' })
    const grid = data.split('\n').map(line => line.split('')).filter(row => row.length > 0 )
    const occupiedPositions = []
    let direction = directions.up
    let position

    // find starting position
    let y = 0
    while (!position) {
        const row = grid[y]
        const x = row.findIndex(symbol => symbol === '^')
        if (x !== -1) position = { x, y }
        else y++
    }

    const getNextPosition = () => {
        return {x: position.x + direction.x, y: position.y + direction.y}
    }

    const isValidPosition = (coordinates) => {
        return coordinates.x >= 0
        && coordinates.x < grid.length
        && coordinates.y >= 0
        && coordinates.y < grid.length
    }

    const changeDirection = () => {
        switch (direction) {
            case directions.up:
                direction = directions.right
                break
            case directions.right:
                direction = directions.down
                break
            case directions.down:
                direction = directions.left
                break
            case directions.left:
                direction = directions.up
                break
        }
    }

    const part1 = () => {
        while(position) {
            const nextPosition = getNextPosition()
            if (isValidPosition(nextPosition)) {
                const nextCharacter = grid[nextPosition.y][nextPosition.x]
                if (nextCharacter !== '#') {
                    occupiedPositions.push(position)
                    position = nextPosition
                } else {
                    changeDirection()
                }
            } else {
                occupiedPositions.push(position)
                position = undefined
            }
        }
        const positionSet = new Set(occupiedPositions.map(coord => JSON.stringify(coord)))
        return positionSet.size
    }

    console.log(`Part 1: ${part1()}`)
}
