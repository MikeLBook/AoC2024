const fs = require('node:fs/promises')

async function day05() {
    const data = await fs.readFile('day05.txt', { encoding: 'utf8' })
    const [ orderData, updateData ] = data.split('\n\n')
    const orderMap = new Map()

    orderData.split('\n').forEach(line => {
        const [key, value] = line.split('|')
        const laterPageNumbers = orderMap.get(key)
        if (laterPageNumbers) {
            laterPageNumbers.push(value)
            orderMap.set(key, laterPageNumbers)
        } else {
            orderMap.set(key, [value])
        }
    })

    const part1 = () => {
        return updateData.split('\n').map(update => {
            if (update.length === 0) return
            const pages = update.split(',')
            const isCorrectUpdate = pages.every((page, index, arr) => {
                if (index === 0) return true
                const laterPageNumbers = orderMap.get(page)
                if (!laterPageNumbers) return true
                return !laterPageNumbers.includes(arr[index - 1])
            })
            if (isCorrectUpdate) return pages
        })
        .filter(result => (result))
        .reduce((sum, update) => sum + parseInt(update[Math.floor(update.length / 2)]), 0)
    }

    const part2 = () => {
    }

    console.log(`Part 1: ${part1()}`)
    console.log(`Part 2: ${part2()}`)
}

day05()
