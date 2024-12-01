const fs = require('node:fs/promises')

async function day1() {
	const data = await fs.readFile('../inputs/day01.txt', { encoding: 'utf8' })
	
	const rows = data.split('\n')
	const listA = []
	const listB = []

	rows.forEach((row, index) => {
		const cols = row
			.split(' ')
			.map(value => parseInt(value))
			.filter(value => !isNaN(value))
            
		if (cols.length !== 0) {
			listA.push(cols[0])
			listB.push(cols[1])
		}
	})

	listA.sort((a, b) => a - b)
	listB.sort((a, b) => a - b)
    
	const part1 = () => {
        return listA.reduce(
            (accumulator, current, index) => {
                return accumulator + Math.abs((listB[index] - current))
            }, 0
        )	
	}	
	

	const part2 = () => {
       return listA.reduce(
           (accumulator, current, index) => {
               return accumulator + (current * listB.filter(value => value === current).length)
           }, 0
        )
	}

	console.log(part1())
	console.log(part2())
}

day1()
