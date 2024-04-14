import request from '@/utils/request'

export function getenviromentvariablesList(params) {
  return request({
    url: '/enviromentvariables',
    method: 'get',
    params
  })
}

export function getenviromentvariablesnum() {
  return request({
    url: '/enviromentvariables/getenviromentvariablesnum',
    method: 'get'
  })
}

export function getenviromentvariablesallList() {
  return request({
    url: '/enviromentvariables/ens',
    method: 'get'
  })
}

export function search(enviromentvariablesForm) {
  return request({
    url: '/enviromentvariables/search',
    method: 'post',
    data: enviromentvariablesForm
  })
}

export function addenviromentvariables(enviromentvariablesForm) {
  return request({
    url: '/enviromentvariables',
    method: 'post',
    data: enviromentvariablesForm
  })
}

export function updateenviromentvariables(enviromentvariablesForm) {
  return request({
    url: '/enviromentvariables/detail',
    method: 'put',
    data: enviromentvariablesForm
  })
}

export function removeenviromentvariables(enviromentvariablesId) {
  return request({
    url: '/enviromentvariables/' + enviromentvariablesId,
    method: 'delete'
  })
}
