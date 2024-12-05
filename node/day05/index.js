const fs = require('node:fs/promises')

day05()

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

    const sumMiddlePageReducer = (sum, update) => {
        return sum + parseInt(update[Math.floor(update.length / 2)])
    }

    const correctPageOrder = (pages) => {
        let index = 0
        while (index < pages.length) {
            if (index === 0) {
                index += 1
            } else {
                let previousPage = pages[index -1]
                let currentPage = pages[index]
                const laterPageNumbers = orderMap.get(currentPage)
                if (laterPageNumbers && laterPageNumbers.includes(previousPage)) {
                    pages[index - 1] = currentPage
                    pages[index] = previousPage
                    if (index > 1) index -= 1
                } else index += 1
            }
        }
        return pages 
    }

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
        .reduce(sumMiddlePageReducer, 0)
    }

    const part2 = () => {
        return updateData.split('\n').map(update => {
            if (update.length === 0) return
            const pages = update.split(',')
            const isIncorrectUpdate = pages.some((page, index, arr) => {
                if (index === 0) return false
                const laterPageNumbers = orderMap.get(page)
                if (!laterPageNumbers) return false
                return laterPageNumbers.includes(arr[index - 1])
            })
            if (isIncorrectUpdate) return correctPageOrder(pages)
        })
        .filter(result => (result))
        .reduce(sumMiddlePageReducer, 0)
    }

    console.log(`Part 1: ${part1()}`)
    console.log(`Part 2: ${part2()}`)
}