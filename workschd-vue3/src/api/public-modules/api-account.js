import service from "@/api/axios-voyagerss.js";

const baesURL = `common/account`

const apiPublicAccount = {
    getTeacherList(fetchQuery) {
        fetchQuery.role = 'EN9DOOR_TEACHER'
        return service.get(`${baesURL}/teacher`, fetchQuery)
    },
    getTeacherById(acconutId) {
        return service.get(`${baesURL}/teacher/${acconutId}`)
    } 
}

export default apiPublicAccount