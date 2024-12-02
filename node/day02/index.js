const fs = require('node:fs/promises')

async function day02() {
    const data = await fs.readFile('day02.txt', { encoding: 'utf8' })

    const isSafeRow = (numericRow) => {
        let allIncreasing = true
        let allDecreasing = true
        let allAdjacent = true
        
        numericRow.forEach((k, i, arr) => {
            if (i > 0) {
                if (k < arr[i - 1]) allIncreasing = false
                if (k > arr[i - 1]) allDecreasing = false
                const difference = Math.abs(k - arr[i - 1])
                if (difference < 1 || difference > 3) allAdjacent = false
            }
        })

        return (allIncreasing || allDecreasing) && allAdjacent 
    }
    
    const part1 = () => {
        const safeRows = data.split('\n').filter(row => {
            if (row.length === 0) return false
            const numericRow = row.split(' ').map(k => parseInt(k))
            return isSafeRow(numericRow)
        })
        
        return safeRows.length
    }

    const part2 = () => {
        function removeIndex(arr, index) {
            return [...arr.slice(0, index), ...arr.slice(index + 1)];
        }
        
        const safeRows = []
        const unsafeRows = []

        data.split('\n').forEach(row => {
            if (row.length > 0) {
                const numericRow = row.split(' ').map(k => parseInt(k))
                if (isSafeRow(numericRow)) safeRows.push(numericRow)
                else unsafeRows.push(numericRow)
            }
        })

        const faultTolerantRows = unsafeRows.filter(row => {
            for (let i = 0; i < row.length; i++) {
                const dampenedRow = removeIndex(row, i)
                if (isSafeRow(dampenedRow)) return true
            }
            return false
        })

        return safeRows.length + faultTolerantRows.length
    }

    console.log(part1())
    console.log(part2())
}

day02()
