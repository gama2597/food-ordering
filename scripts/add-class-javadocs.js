const fs = require('fs')
const path = require('path')

const base = path.resolve(__dirname, '..')

function walk(dir, acc = []) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name)
    if (entry.isDirectory()) {
      walk(full, acc)
    } else if (entry.isFile() && full.endsWith('.java')) {
      acc.push(full)
    }
  }
  return acc
}

function needsFile(filePath) {
  return /src[\\/]main[\\/]java/.test(filePath)
    && (
      /Application\.java$/.test(filePath)
      || /[\\/]infrastructure[\\/]config[\\/][^\\/]+\.java$/.test(filePath)
      || /[\\/]presentation[\\/]controller[\\/][^\\/]+\.java$/.test(filePath)
      || /[\\/]application[\\/]service[\\/].+\.java$/.test(filePath)
    )
}

function getDescription(relativePath) {
  if (relativePath.includes('/presentation/controller/')) {
    return 'Controlador REST que expone endpoints HTTP del microservicio y delega la logica al servicio de aplicacion.'
  }
  if (relativePath.includes('/infrastructure/config/')) {
    return 'Configuracion tecnica del microservicio (seguridad, OpenAPI, Kafka o integraciones de infraestructura).'
  }
  if (relativePath.includes('/application/service/')) {
    return 'Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.'
  }
  if (relativePath.endsWith('Application.java')) {
    return 'Punto de entrada del microservicio. Inicia el contexto Spring Boot y el ciclo de vida de la aplicacion.'
  }
  return 'Componente de la arquitectura del microservicio.'
}

const allJava = walk(path.join(base, 'backend'))
const targets = allJava.filter(needsFile)

let changed = 0
for (const filePath of targets) {
  const original = fs.readFileSync(filePath, 'utf8')
  const lines = original.split(/\r?\n/)

  const declarationIndex = lines.findIndex((line) => /public\s+(class|interface|record|enum)\s+\w+/.test(line))
  if (declarationIndex < 0) {
    continue
  }

  let top = declarationIndex - 1
  while (top >= 0 && lines[top].trim().startsWith('@')) {
    top -= 1
  }

  let hasJavadoc = false
  if (top >= 0 && lines[top].trim().endsWith('*/')) {
    for (let j = top; j >= 0; j -= 1) {
      if (lines[j].includes('/**')) {
        hasJavadoc = true
        break
      }
      if (lines[j].trim() === '') {
        break
      }
    }
  }

  if (hasJavadoc) {
    continue
  }

  const relativePath = filePath.replace(base, '').replace(/\\/g, '/')
  const description = getDescription(relativePath)
  const indent = (lines[declarationIndex].match(/^\s*/) || [''])[0]
  const javadoc = [
    `${indent}/**`,
    `${indent} * ${description}`,
    `${indent} */`
  ]

  let insertAt = declarationIndex
  while (insertAt > 0 && lines[insertAt - 1].trim().startsWith('@')) {
    insertAt -= 1
  }

  lines.splice(insertAt, 0, ...javadoc)
  fs.writeFileSync(filePath, `${lines.join('\n')}\n`, 'utf8')
  changed += 1
}

console.log(`updated ${changed} files out of ${targets.length}`)
