import request from '@/utils/request'

export function getmacdepunitList(params) {
  return request({
    url: '/macdepunit',
    method: 'get',
    params
  })
}

export function search(macdepunitForm) {
  return request({
    url: '/macdepunit/search',
    method: 'post',
    data: macdepunitForm
  })
}

export function addenvassemble(macdepunitForm) {
  return request({
    url: '/macdepunit/addenvassemble',
    method: 'post',
    data: macdepunitForm
  })
}

export function findMacAndDepWithEnv(macdepunitForm) {
  return request({
    url: '/macdepunit/findMacAndDepWithEnv',
    method: 'post',
    data: macdepunitForm
  })
}

export function findMacAndAssembleWithEnv(macdepunitForm) {
  return request({
    url: '/macdepunit/findMacAndAssembleWithEnv',
    method: 'post',
    data: macdepunitForm
  })
}

export function addmacdepunit(macdepunitForm) {
  return request({
    url: '/macdepunit',
    method: 'post',
    data: macdepunitForm
  })
}

export function updatemacdepunit(macdepunitForm) {
  return request({
    url: '/macdepunit/detail',
    method: 'put',
    data: macdepunitForm
  })
}

export function updatemacassemble(macdepunitForm) {
  return request({
    url: '/macdepunit/detailassemble',
    method: 'put',
    data: macdepunitForm
  })
}

export function removemacdepunit(macdepunitId) {
  return request({
    url: '/macdepunit/' + macdepunitId,
    method: 'delete'
  })
}

export function deleteassemble(macdepunitId) {
  return request({
    url: '/macdepunit/deleteassemble',
    method: 'post',
    data: macdepunitId
  })
}
