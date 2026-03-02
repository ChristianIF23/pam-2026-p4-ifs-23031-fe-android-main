package org.delcom.pam_p4_ifs23031.helper

class ConstHelper {
    // Route Names
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),
        GenreMusik(path = "genre-musik"),
        GenreMusikAdd(path = "genre-musik/add"),
        GenreMusikDetail(path = "genre-musik/{genreMusikId}"),
        GenreMusikEdit(path = "genre-musik/{genreMusikId}/edit"),
        Plants(path = "plants"),
        PlantsAdd(path = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path = "plants/{plantId}/edit"),

    }
}