import request from '@/utils/request'

export function getapicaseresponesettingList(params) {
  return request({
    url: '/apicaseresponesetting',
    method: 'get',
    params
  })
}

export function getapicaseresponesettingLists(params) {
  return request({
    url: '/apicaseresponesetting/getapicaseresponesetting',
    method: 'get',
    params
  })
}

export function getapicaseresponesettingnum(params) {
  return request({
    url: '/apicaseresponesetting/getapicaseresponesettingnum',
    method: 'get',
    params
  })
}

export function search(apicaseresponesettingForm) {
  return request({
    url: '/apicaseresponesetting/search',
    method: 'post',
    data: apicaseresponesettingForm
  })
}

export function addapicaseresponesetting(apicaseresponesettingForm) {
  return request({
    url: '/apicaseresponesetting',
    method: 'post',
    data: apicaseresponesettingForm
  })
}

export function updateapicaseresponesetting(apicaseresponesettingForm) {
  return request({
    url: '/apicaseresponesetting/detail',
    method: 'put',
    data: apicaseresponesettingForm
  })
}

export function removeapicaseresponesetting(apicaseresponesettingId) {
  return request({
    url: '/apicaseresponesetting/' + apicaseresponesettingId,
    method: 'delete'
  })
}
