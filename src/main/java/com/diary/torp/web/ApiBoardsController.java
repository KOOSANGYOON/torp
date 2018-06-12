package com.diary.torp.web;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.domain.*;
import com.diary.torp.security.LoginUser;
import com.diary.torp.service.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/boards")
public class ApiBoardsController {
    private static final Logger log = LoggerFactory.getLogger(ApiBoardsController.class);

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @Resource(name = "toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @PostMapping("/add")
    public ToDoBoard createBoard(@LoginUser User loginUser, @Valid @RequestBody String title) {
        log.debug("user is " + loginUser);
        log.debug("title is " + title);

        ToDoBoard newToDoBoard = toDoService.createBoard(loginUser, title);

        return newToDoBoard;
    }

    @PostMapping("/{id}/add")
    public ToDoDeck createDeck(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody String title) {
        log.debug("user is " + loginUser);
        log.debug("title is " + title);

        ToDoDeck newToDoDeck = null;
        try {
            newToDoDeck = toDoService.createDeck(id, loginUser, title);
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
        }
        toDoService.addDeck(id, newToDoDeck);

        return newToDoDeck;
    }

    @PostMapping("/{boardId}/{deckId}/add")
    public ToDoCard createCard(@LoginUser User loginUser, @PathVariable long boardId, @PathVariable long deckId, @Valid @RequestBody String title) {
        log.debug("user is " + loginUser);
        log.debug("board id is " + boardId);
        log.debug("deck id is " + deckId);
        log.debug("title is " + title);

        ToDoCard newCard = null;
        try {
            newCard = toDoService.createCard(deckId, loginUser, title);
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
        }
        toDoService.addCard(deckId, newCard);

        return newCard;
    }

    @PostMapping("/{boardId}/{deckId}/{cardId}/cardInfo")
    public ToDoCard getCardInfo(@LoginUser User loginUser, @PathVariable long boardId, @PathVariable long deckId,
                                @PathVariable long cardId) throws Exception {
        return toDoService.getCardInfo(loginUser, cardId);
    }

    @PostMapping("/{boardId}/{deckId}/{cardId}/editDescription")
    public ToDoCard editDescription(@LoginUser User loginUser, @PathVariable long boardId, @PathVariable long deckId,
                                    @PathVariable long cardId, @Valid @RequestBody String description) throws UnAuthenticationException {
        log.debug("user is " + loginUser);
        log.debug("board id is " + boardId);
        log.debug("deck id is " + deckId);
        log.debug("card id is " + cardId);
        log.debug("new description is " + description);

        return toDoService.editDescription(loginUser, cardId, description);
    }

    @PostMapping("/{boardId}/{deckId}/{cardId}/addComment")
    public Comment addComment(@LoginUser User loginUser, @PathVariable long boardId, @PathVariable long deckId, @PathVariable long cardId,
                                    @Valid @RequestBody String comment) throws UnAuthenticationException {
        log.debug("user is " + loginUser);
        log.debug("board id is " + boardId);
        log.debug("deck id is " + deckId);
        log.debug("card id is " + cardId);
        log.debug("new comment is " + comment);

        Comment newComment = toDoService.createComment(loginUser, comment);
        return toDoService.addComment(loginUser, cardId, newComment);
    }

    @PutMapping("/{boardId}")
    public ToDoBoard editBoardTitle(@LoginUser User loginUser, @PathVariable long boardId,
                                   @Valid @RequestBody String newTitle) throws UnAuthenticationException {
        return toDoService.editBoardTitle(loginUser, boardId, newTitle);
    }

    @PutMapping("/{boardId}/{deckId}")
    public ToDoDeck editDeckTitle(@LoginUser User loginUser, @PathVariable long boardId, @PathVariable long deckId,
                                  @Valid @RequestBody String newTitle) throws UnAuthenticationException {
        return toDoService.editDeckTitle(loginUser, deckId, newTitle);
    }

    @PutMapping("/{boardId}/{deckId}/{cardId}")
    public ToDoCard editCardTitle(@LoginUser User loginUser, @PathVariable long boardId, @PathVariable long deckId,
                                  @PathVariable long cardId, @Valid @RequestBody String newTitle) throws UnAuthenticationException {
        log.debug("in editCardTitle controller.");
        return toDoService.editCardTitle(loginUser, cardId, newTitle);
    }

    @DeleteMapping("/{boardId}")
    public ToDoBoard deleteBoard(@LoginUser User loginUser, @PathVariable long boardId,
                                 @Valid @RequestBody String password) throws LoginException, UnAuthenticationException {
        log.debug("api controller - delete board");
        if (!loginUser.matchPassword(password)) {
            log.debug("password is wrong.");
            throw new LoginException("password isn't correct.");
        }
        return toDoService.deleteBoard(loginUser, boardId);
    }
}
