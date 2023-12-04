import request from '@/utils/request'

export function getexecuteplanbatchList(params) {
  return request({
    url: '/executeplanbatch',
    method: 'get',
    params
  })
}

export function getbatchbyplan(params) {
  return request({
    url: '/executeplanbatch/getbatchbyplan',
    method: 'post',
    params
  })
}

export function updatebatchstatus(params) {
  return request({
    url: '/executeplanbatch/updatebatchstatus',
    method: 'post',
    data: params
  })
}

export function getstopplanbatchList(params) {
  return request({
    url: '/executeplanbatch/getstopplanbatchList',
    method: 'post',
    params
  })
}

export function search(executeplanbatchForm) {
  return request({
    url: '/executeplanbatch/search',
    method: 'post',
    data: executeplanbatchForm
  })
}

export function addexecuteplanbatch(executeplanbatchForm) {
  return request({
    url: '/executeplanbatch',
    method: 'post',
    data: executeplanbatchForm
  })
}
