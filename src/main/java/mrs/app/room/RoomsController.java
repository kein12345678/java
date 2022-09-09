package mrs.app.room;

import java.time.LacalDate;
import java.time.LocalDate;
import java.until.List;

import mrs.domain.model.converter.ReservableRoom;
import mrs.domain.service.room.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("rooms")
public class RoomsController {
	@Autowired
	RoomService roomService;
		
	@RequestMapping(path = "{date}", method = RequestMethod.GET)
	String listRooms(
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date, Model model) {
		List<ReservableRoom> rooms = roomService.findReservableRooms(date);
		model.addAllAttributes("room", rooms);
		return "room/listRooms";
	}
}
