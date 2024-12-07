/**
 * I love this problem but hate this solution. 
 * I'll need to revisit this one.
 * The run time on part 2 is atrocious.
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

    let occupiedPositions = []
    let startingPosition = undefined
    let position = undefined
    let direction = directions.up
    let obstruction = undefined

    // find starting position
    let y = 0
    while (!position) {
        const row = grid[y]
        const x = row.findIndex(symbol => symbol === '^')
        if (x !== -1) {
            position = { x, y }
            startingPosition = { x, y }
        } else y++
    }

    const getNextPosition = () => {
        return {x: position.x + direction.x, y: position.y + direction.y}
    }

    const isValidGridPosition = (coordinates) => {
        return coordinates.x >= 0
        && coordinates.x < grid.length
        && coordinates.y >= 0
        && coordinates.y < grid.length
    }

    const isObstructedPosition = (coordinates) => {
        return grid[coordinates.y][coordinates.x] === '#' || JSON.stringify(coordinates) === JSON.stringify(obstruction)
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

    const hasDejaVu = () => {
        return occupiedPositions.includes(JSON.stringify([position, direction]))
    }

    const walkPatrolRoute = () => {
        while(position) {
            if (hasDejaVu()) break
            const nextPosition = getNextPosition()
            if (isValidGridPosition(nextPosition)) {
                if (!isObstructedPosition(nextPosition)) {
                    occupiedPositions.push(JSON.stringify([position, direction]))
                    position = nextPosition
                } else {
                    changeDirection()
                }
            } else {
                occupiedPositions.push(JSON.stringify([position, direction]))
                position = undefined
            }
        }
    }

    const part1 = () => {
        walkPatrolRoute()
        const positionSet = new Set(occupiedPositions.map(pos => JSON.stringify(JSON.parse(pos)[0])))
        return positionSet.size
    }

    const part2 = () => {
        let successfulObstructions = 0
        for (let y = 0; y < grid.length; y++) {
            for (let x = 0; x < grid.length; x++) {
                if (!(grid[y][x] === '#')) {
                    occupiedPositions = []
                    position = startingPosition
                    direction = directions.up
                    obstruction = {x, y}
                    console.log(obstruction)
                    walkPatrolRoute()
                    if (position) {
                        successfulObstructions++
                    }
                }
            }
        }
        return successfulObstructions
    }

    console.log(`Part 1: ${part1()}`)
    console.log(`Part 2: ${part2()}`)
}
