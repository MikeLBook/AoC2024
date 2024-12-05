const fs = require('node:fs/promises')

day05()

/**
 * Probably overdue that I setup an LSP and start using TypeScript 
 * if I'm going to keep doing these in neovim...
 */

async function day05() {
    const data = await fs.readFile('day05.txt', { encoding: 'utf8' })
    const [ orderData, updateData ] = data.split('\n\n')
    const orderMap = new Map() // Maps a given page number to the list of page numbers that must come after it

    // Fill the map
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
        const orderedPages = updateData.split('\n').map(update => {
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

        return sumMiddlePages(orderedPages)
    }

    const part2 = () => {
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

        const reorderedPages = updateData.split('\n').map(update => {
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
        
        return sumMiddlePages(reorderedPages)
    }

    const sumMiddlePages = (pages) => pages.filter(result => (result))
    .reduce((sum, update) => sum + parseInt(update[Math.floor(update.length / 2)]), 0)

    console.log(`Part 1: ${part1()}`)
    console.log(`Part 2: ${part2()}`)
}