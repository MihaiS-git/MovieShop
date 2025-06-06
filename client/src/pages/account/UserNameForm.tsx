import { useUpdateUserMutation } from "@/features/users/userApi";
import { UserDetails } from "@/types/User";
import { useEffect, useState } from "react";

type Props = {
  user: UserDetails;
};

const UserNameForm = ({ user }: Props) => {
    const [form, setForm] = useState({firstName: "", lastName: ""});

  useEffect(() => {
    setForm({
        firstName: user.firstName || "",
        lastName: user.lastName || ""
    });
  }, [user]);

  const [updateUser, { isLoading: isUpdating }] = useUpdateUserMutation();

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const {name, value} = e.target;
    setForm((prev) => ({
        ...prev,
        [name]: value === "None" ? "" : value,
    }));
  };

  const handleSubmitForm = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!form.firstName.trim()) {
      alert("First Name is required");
      return;
    }

    if (!form.lastName.trim()) {
      alert("Last Name is required");
      return;
    }

    try {
      await updateUser({
        id: user.id,
        data: {
          firstName: form.firstName,
          lastName: form.lastName,
        },
      }).unwrap();
      alert("User updated successfully.");
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : "Unknown error";
      alert(`Update failed: ${message}`);
    }
  };

  return (
    <div className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 rounded-lg p-4">
      <h2 className="text-xl font-semibold text-gold-500 py-4 pb-8">
        User Name
      </h2>
      <form onSubmit={handleSubmitForm} className="flex flex-col">
        <div className="my-1">
          <label htmlFor="firstName">First Name</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={form.firstName}
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4"
          />
        </div>

        <div className="my-1">
          <label htmlFor="lastName">Last Name</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={form.lastName}
            onChange={handleInputChange}
            className="bg-gray-300 text-charcoal-800 p-1 px-2 rounded-sm ms-4"
          />
        </div>

        <button
          type="submit"
          disabled={isUpdating}
          className="bg-red-500 dark:bg-charcoal-800 text-charcoal-800 dark:text-red-500 px-2 py-1 mt-4 rounded-sm mx-auto"
        >
          {isUpdating ? "Updating" : "Save changes"}
        </button>
      </form>
    </div>
  );
};

export default UserNameForm;
