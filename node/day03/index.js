const fs = require('node:fs/promises')

async function day03() {
    const data = await fs.readFile('day03.txt', { encoding: 'utf8' })

    const part1 = () => {
        const regex = /mul\((\d{1,3}),(\d{1,3})\)/g
        const muls = data.matchAll(regex)
        let sum = 0
        for (const mul of muls) {
            sum += mul[1] * mul[2]
        }
        return sum
    }

    const part2 = () => {
        const regex = /mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)/g
        const instructions = data.matchAll(regex)
        let sum = 0
        let enabled = true
        for (const instruction of instructions) {
            if (instruction[0] === "do()") {
                enabled = true
            } else if (instruction[0] === "don't()") {
                enabled = false
            } else {
                if (enabled) {
                    sum += instruction[1] * instruction[2]
                }
            }
        }
        return sum
    }

    console.log(`Part 1: ${part1()}`)
    console.log(`Part 2: ${part2()}`)
}

day03()
