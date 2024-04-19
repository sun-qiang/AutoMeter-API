import request from '@/utils/request'

export function getapicasesdbassertList(params) {
  return request({
    url: '/apicases/dbassert',
    method: 'get',
    params
  })
}

export function searchdbassert(apicasesdbassertForm) {
  return request({
    url: '/apicases/dbassert/search',
    method: 'post',
    data: apicasesdbassertForm
  })
}

export function batchdbassertapicase(apicasesForm) {
  return request({
    url: '/apicases/batchdbassertapicase',
    method: 'post',
    data: apicasesForm
  })
}

export function getdbassertbycaseid(params) {
  return request({
    url: '/apicases/dbassert/getdbassertbycaseid',
    method: 'post',
    data: params
  })
}

export function addapicasesdbassert(apicasesdbassertForm) {
  return request({
    url: '/apicases/dbassert',
    method: 'post',
    data: apicasesdbassertForm
  })
}

export function updateapicasesdbassert(apicasesdbassertForm) {
  return request({
    url: '/apicases/dbassert/detail',
    method: 'put',
    data: apicasesdbassertForm
  })
}

export function removeapicasesdbassert(apicasesdbassertId) {
  return request({
    url: '/apicases/dbassert/' + apicasesdbassertId,
    method: 'delete'
  })
}
