import request from '@/utils/request'

export function getdelayconditionList(params) {
  return request({
    url: '/condition/delay',
    method: 'get',
    params
  })
}

export function getdelayconditionnum() {
  return request({
    url: '/condition/delay/getdelayconditionnum',
    method: 'get'
  })
}

export function getdelayconditionallList() {
  return request({
    url: '/condition/delay/ens',
    method: 'get'
  })
}

export function search(delayconditionForm) {
  return request({
    url: '/condition/delay/search',
    method: 'post',
    data: delayconditionForm
  })
}

export function searchbytype(delayconditionForm) {
  return request({
    url: '/condition/delay/searchbytype',
    method: 'post',
    data: delayconditionForm
  })
}

export function adddelaycondition(delayconditionForm) {
  return request({
    url: '/condition/delay',
    method: 'post',
    data: delayconditionForm
  })
}

export function updatedelaycondition(delayconditionForm) {
  return request({
    url: '/condition/delay/detail',
    method: 'put',
    data: delayconditionForm
  })
}

export function removedelaycondition(delayconditionId) {
  return request({
    url: '/condition/delay/' + delayconditionId,
    method: 'delete'
  })
}
