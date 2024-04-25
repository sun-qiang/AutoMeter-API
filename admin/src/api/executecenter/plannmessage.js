import request from '@/utils/request'

export function getplannmessageparamList(params) {
  return request({
    url: '/plannmessage',
    method: 'get',
    params
  })
}

export function searchparamsbyepid(params) {
  return request({
    url: '/plannmessage/searchparamsbyepid',
    method: 'post',
    data: params
  })
}

export function search(plannmessageForm) {
  return request({
    url: '/plannmessage/search',
    method: 'post',
    data: plannmessageForm
  })
}

export function addplannmessageparam(plannmessageForm) {
  return request({
    url: '/plannmessage',
    method: 'post',
    data: plannmessageForm
  })
}

export function updateplannmessageparams(plannmessageForm) {
  return request({
    url: '/plannmessage/detail',
    method: 'put',
    data: plannmessageForm
  })
}

export function removeplannmessageparam(plannmessageId) {
  return request({
    url: '/plannmessage/' + plannmessageId,
    method: 'delete'
  })
}
