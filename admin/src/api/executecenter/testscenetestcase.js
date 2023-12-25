import request from '@/utils/request'

export function gettestscenecaList(params) {
  return request({
    url: '/testscene/testcase',
    method: 'get',
    params
  })
}

export function addtestscenetestcase(sceneForm) {
  return request({
    url: '/testscene/testcase/addcases',
    method: 'post',
    data: sceneForm
  })
}

export function updatescenenCaseorder(executeplanForm) {
  return request({
    url: '/testscene/testcase/updatescenenCaseorder',
    method: 'post',
    data: executeplanForm
  })
}

export function updatescenecaselogic(executeplanForm) {
  return request({
    url: '/testscene/testcase/updatescenecaselogic',
    method: 'post',
    data: executeplanForm
  })
}

export function findscenecasebyid(executeplanForm) {
  return request({
    url: '/testscene/testcase/findscenecasebyid',
    method: 'post',
    data: executeplanForm
  })
}

export function getstaticsplan(params) {
  return request({
    url: '/testscene/testcase/getstaticsplan',
    method: 'get',
    params
  })
}

export function gettestscenenum(params) {
  return request({
    url: '/testscene/testcase/gettestscenenum',
    method: 'get',
    params
  })
}

export function getallexplanbytype(params) {
  return request({
    url: '/testscene/testcase/getallexplanbytype',
    method: 'get',
    params
  })
}

export function getallexplan(params) {
  return request({
    url: '/testscene/testcase/getallexplan',
    method: 'get',
    params
  })
}

export function search(testsceneForm) {
  return request({
    url: '/testscene/testcase/search',
    method: 'post',
    data: testsceneForm
  })
}

export function findcasebyscenenid(testsceneForm) {
  return request({
    url: '/testscene/testcase/findcasebyscenenid',
    method: 'post',
    data: testsceneForm
  })
}

export function addtestscene(testsceneForm) {
  return request({
    url: '/testscene/testcase',
    method: 'post',
    data: testsceneForm
  })
}

export function testscene(testsceneForm) {
  return request({
    url: '/testscene/testcase/execplancases',
    method: 'post',
    data: testsceneForm
  })
}

export function checkplancondition(testsceneForm) {
  return request({
    url: '/testscene/testcase/checkcondition',
    method: 'post',
    data: testsceneForm
  })
}

export function updatetestscene(testsceneForm) {
  return request({
    url: '/testscene/testcase/detail',
    method: 'put',
    data: testsceneForm
  })
}

export function updatetestscenestatus(testsceneForm) {
  return request({
    url: '/testscene/testcase/updatestatus',
    method: 'post',
    data: testsceneForm
  })
}

export function removetestscenecase(testsceneId) {
  return request({
    url: '/testscene/testcase/' + testsceneId,
    method: 'delete'
  })
}
