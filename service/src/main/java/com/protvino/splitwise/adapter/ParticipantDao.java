package com.protvino.splitwise.adapter;

import com.protvino.splitwise.domain.request.EditParticipantRequest;
import com.protvino.splitwise.domain.value.Participant;

public interface ParticipantDao {

    long create(EditParticipantRequest editParticipantRequest);

    //Нужен ли тут вообще update метод? (Только если мы будем менять на null после удаления, но вроде в бд это автоматически делается
    void update(long id, EditParticipantRequest editParticipantRequest);

    //Вопрос, мы же когда будем удалять, записи из этой таблицы, мы будем делать это, когда будем удалять группу из приложения
    //Когда будем удалять человека, скорее всего просто везде его id будет заменяться на null?
    void delete(EditParticipantRequest editParticipantRequest);

    void delete(long id);

    Participant findById(Long id);
}
