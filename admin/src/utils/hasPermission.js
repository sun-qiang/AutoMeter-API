import store from '../store'

export function hasPermission(permission) {
  return store.getters.permissionCodeList.indexOf(permission) >= 0
}

export function getOperatorWidth() {
  // 默认宽度
  let width = 50
  // 内间距
  const paddingSpacing = 8
  // 按钮数量
  let buttonCount = 0

  let operatorColumn = document.getElementsByClassName('optionDiv')
  // 如果节点数量大于0-循环这个节点，
  operatorColumn = Array.from(operatorColumn)
  if (operatorColumn.length > 0) {
    operatorColumn.forEach(item => {
      // 最宽的宽度
      width = width > item.offsetWidth ? width : item.offsetWidth
      // 计算 <el-button> 标签的数量
      const buttons = item.getElementsByClassName('el-button')
      buttonCount = buttons.length
      buttonCount = buttonCount > buttons.length ? buttonCount : buttons.length
    })

    // 如果按钮数量大于2，宽度要加上边距*（按钮数量-1）
    if (buttonCount > 2) {
      width += (paddingSpacing * (buttonCount - 1))
    }
  }
  return width
}
