import request from '@/utils/request'

export function gettestsceneList(params) {
  return request({
    url: '/testscene',
    method: 'get',
    params
  })
}

export function getstaticsplan(params) {
  return request({
    url: '/testscene/getstaticsplan',
    method: 'get',
    params
  })
}

export function gettestscenenum(params) {
  return request({
    url: '/testscene/gettestscenenum',
    method: 'get',
    params
  })
}

export function getallexplanbytype(params) {
  return request({
    url: '/testscene/getallexplanbytype',
    method: 'get',
    params
  })
}

export function getallexplan(params) {
  return request({
    url: '/testscene/getallexplan',
    method: 'get',
    params
  })
}

export function search(testsceneForm) {
  return request({
    url: '/testscene/search',
    method: 'post',
    data: testsceneForm
  })
}

export function searchscenetreedata(testsceneForm) {
  return request({
    url: '/testscene/searchscenetreedata',
    method: 'post',
    data: testsceneForm
  })
}

export function addtestscene(testsceneForm) {
  return request({
    url: '/testscene',
    method: 'post',
    data: testsceneForm
  })
}

export function testscene(testsceneForm) {
  return request({
    url: '/testscene/execplancases',
    method: 'post',
    data: testsceneForm
  })
}

export function checkplancondition(testsceneForm) {
  return request({
    url: '/testscene/checkcondition',
    method: 'post',
    data: testsceneForm
  })
}

export function updatetestscene(testsceneForm) {
  return request({
    url: '/testscene/detail',
    method: 'put',
    data: testsceneForm
  })
}

export function copyscene(apicasesForm) {
  return request({
    url: '/testscene/copyscene',
    method: 'post',
    data: apicasesForm
  })
}

export function getsceneallList(params) {
  return request({
    url: '/testscene/scenes',
    method: 'get',
    params
  })
}

export function updatetestscenestatus(testsceneForm) {
  return request({
    url: '/testscene/updatestatus',
    method: 'post',
    data: testsceneForm
  })
}

export function removetestscene(testsceneId) {
  return request({
    url: '/testscene/' + testsceneId,
    method: 'delete'
  })
}
