package com.poultry.broiler.domain.model

/**
 * Identifies the cardinal building orientation of a broiler house.
 *
 * Orientation is captured at 8-point granularity (45° increments) which is
 * sufficient for solar-gain calculations while remaining intuitive to select
 * on the compass UI.
 *
 * @property displayNameFr The French display label (full compass-point name).
 * @property degrees The compass heading in degrees, measured clockwise from North.
 */
enum class HouseOrientation(val displayNameFr: String, val degrees: Int) {
    N("Nord", 0),
    NE("Nord-Est", 45),
    E("Est", 90),
    SE("Sud-Est", 135),
    S("Sud", 180),
    SW("Sud-Ouest", 225),
    W("Ouest", 270),
    NW("Nord-Ouest", 315),
}
