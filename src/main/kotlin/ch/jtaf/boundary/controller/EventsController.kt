package ch.jtaf.boundary.controller

import ch.jtaf.control.repository.EventRepository
import ch.jtaf.control.repository.OrganizationRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class EventsController(private val eventRepository: EventRepository,
                       private val organizationRepository: OrganizationRepository) {

    @GetMapping("/sec/{organization}/events")
    fun get(@AuthenticationPrincipal user: User,
            @PathVariable("organization") organizationKey: String,
            @RequestParam("mode") mode: String?,
            @RequestParam("categoryId") seriesId: Long?,
            @RequestParam("categoryId") categoryId: Long?): ModelAndView {
        val mav = ModelAndView("/sec/events")

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["events"] = eventRepository.findByOrganizationId(organization.id!!)

        mav.model["mode"] = mode ?: "edit"
        if (seriesId != null) {
            mav.model["seriesId"] = seriesId
        }
        if (categoryId != null) {
            mav.model["categoryId"] = categoryId
        }

        return mav
    }

    @GetMapping("/sec/{organization}/events/{eventId}/delete")
    fun deleteById(@AuthenticationPrincipal user: User,
                   @PathVariable("organization") organizationKey: String,
                   @PathVariable("eventId") eventId: Long): ModelAndView {
        eventRepository.deleteById(eventId)

        val mav = ModelAndView("/sec/events")

        val organization = organizationRepository.findByKey(organizationKey)
        mav.model["events"] = eventRepository.findByOrganizationId(organization.id!!)

        mav.model["mode"] = "edit"
        return mav
    }

}
