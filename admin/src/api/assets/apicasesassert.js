import request from '@/utils/request'

export function getapicasesassertList(params) {
  return request({
    url: '/apicases/assert',
    method: 'get',
    params
  })
}

export function searchassert(apicasesassertForm) {
  return request({
    url: '/apicases/assert/search',
    method: 'post',
    data: apicasesassertForm
  })
}

export function batchassertapicase(apicasesForm) {
  return request({
    url: '/apicases/batchassertapicase',
    method: 'post',
    data: apicasesForm
  })
}

export function getassertbycaseid(params) {
  return request({
    url: '/apicases/assert/getassertbycaseid',
    method: 'post',
    data: params
  })
}

export function addapicasesassert(apicasesassertForm) {
  return request({
    url: '/apicases/assert',
    method: 'post',
    data: apicasesassertForm
  })
}

export function updateapicasesassert(apicasesassertForm) {
  return request({
    url: '/apicases/assert/detail',
    method: 'put',
    data: apicasesassertForm
  })
}

export function removeapicasesassert(apicasesassertId) {
  return request({
    url: '/apicases/assert/' + apicasesassertId,
    method: 'delete'
  })
}
