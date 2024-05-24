import request from '@/utils/request'

export function getplanmailparamList(params) {
  return request({
    url: '/planmail',
    method: 'get',
    params
  })
}

export function searchparamsbyepid(params) {
  return request({
    url: '/planmail/searchparamsbyepid',
    method: 'post',
    data: params
  })
}

export function search(planmailForm) {
  return request({
    url: '/planmail/search',
    method: 'post',
    data: planmailForm
  })
}

export function addbatchmail(sceneForm) {
  return request({
    url: '/planmail/addbatchmail',
    method: 'post',
    data: sceneForm
  })
}

export function addplanmailparam(planmailForm) {
  return request({
    url: '/planmail',
    method: 'post',
    data: planmailForm
  })
}

export function updateplanmailparams(planmailForm) {
  return request({
    url: '/planmail/detail',
    method: 'put',
    data: planmailForm
  })
}

export function removeplanmailparam(planmailId) {
  return request({
    url: '/planmail/' + planmailId,
    method: 'delete'
  })
}
