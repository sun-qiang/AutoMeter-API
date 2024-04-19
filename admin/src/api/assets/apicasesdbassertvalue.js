import request from '@/utils/request'

export function getapicasesdbassertvalueList(params) {
  return request({
    url: '/apicases/dbassert/value',
    method: 'get',
    params
  })
}

export function searchdbassertvalue(apicasesdbassertvalueForm) {
  return request({
    url: '/apicases/dbassert/value/search',
    method: 'post',
    data: apicasesdbassertvalueForm
  })
}

export function batchdbassertvalueapicase(apicasesForm) {
  return request({
    url: '/apicases/batchdbassert/valueapicase',
    method: 'post',
    data: apicasesForm
  })
}

export function getdbassertvaluebycaseid(params) {
  return request({
    url: '/apicases/dbassert/value/getdbassert/valuebycaseid',
    method: 'post',
    data: params
  })
}

export function addapicasesdbassertvalue(apicasesdbassertvalueForm) {
  return request({
    url: '/apicases/dbassert/value',
    method: 'post',
    data: apicasesdbassertvalueForm
  })
}

export function updateapicasesdbassertvalue(apicasesdbassertvalueForm) {
  return request({
    url: '/apicases/dbassert/value/detail',
    method: 'put',
    data: apicasesdbassertvalueForm
  })
}

export function removeapicasesdbassertvalue(apicasesdbassertvalueId) {
  return request({
    url: '/apicases/dbassert/value/' + apicasesdbassertvalueId,
    method: 'delete'
  })
}
